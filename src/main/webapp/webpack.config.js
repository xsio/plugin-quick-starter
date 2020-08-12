module.exports = require('./webpack.config.common.js')({
    title: 'sample',
    css: []
});
module.exports.entry.index = "./src/index.js";
//module.exports.externals.echarts = 'echarts';
var HtmlWebpackPlugin = require('html-webpack-plugin');
module.exports.plugins.find(p => p instanceof HtmlWebpackPlugin).options.filename = "index.html";
