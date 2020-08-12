process.env.NODE_ENV = "development";

var target = process.argv[2];

const path = require('path');
const Webpack = require('webpack');
const WebpackDevServer = require('webpack-dev-server');
const webpackConfig = require(`./webpack.config`);
webpackConfig.resolve.modules = [path.resolve(__dirname, 'node_modules'), 'node_modules'];
webpackConfig.entry['mount'] = `webpack-dev-server/client?http://localhost:3003/application/${target}/sockjs-node`;
webpackConfig.plugins.push(new Webpack.HotModuleReplacementPlugin());

const compiler = Webpack(webpackConfig);
const server = new WebpackDevServer(compiler, {
    stats: {
        colors: true
    }
});

server.listen(0, '127.0.0.1', () => {
    const port = server.listeningApp.address().port;
    console.log(`dev server running at http://localhost:${port}`);

    var request = require('request');
    request.put(`http://localhost:3003/__dev/application/${target}`, { body: port, json: true }, (err, response, body) => {
        if (err)
            console.log(`fail to mount.\n${err.message}`);
        else
            if (response.statusCode == 201)
                console.log("successfully mounted.");
            else
                console.log(`fail to mount.\n${body}`);
        if (err || response.statusCode != 201) {
            server.close();
            process.exit();
        }
    });

    process.on('SIGINT', () => {
        request.delete(`http://localhost:3003/__dev/application/${target}`, (err, response, body) => {
            if (err)
                console.log(`fail to unmount.\n${err.message}`);
            else
                if (response.statusCode == 204)
                    console.log("successfully unmounted.");
                else
                    console.log(`fail to unmount.\n${body}`);
            server.close();
            process.exit();
        });
    });
});
