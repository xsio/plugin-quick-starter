'use strict';

process.env.NODE_ENV = process.env.NODE_ENV || 'development';

const path = require('path');
const child_process = require('child_process');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ModuleScopePlugin = require('react-dev-utils/ModuleScopePlugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

var babelLoader;
const webpackConfig = {
    entry: {
        index: './src/index.js',
        dependencies: './dependencies.js'
    },
    output: undefined,
    resolve: {
        alias: {}
    },
    module: {
        rules: [
            babelLoader = {
                test: /\.js$/,
                loader: 'babel-loader'
            },
            {
                test: /\.(css|less)$/,
                use: ['style-loader', 'css-loader', 'less-loader']
            },
            {
                test: [/\.bmp$/, /\.gif$/, /\.jpe?g$/, /\.png$/],
                loader: require.resolve('url-loader'),
                options: {
                    limit: 10000,
                    name: 'static/media/[name].[hash:8].[ext]',
                },
            },
            {
                test: () => true,
                issuer: path.resolve(__dirname, "./dependencies.js"),
                loader: 'file-loader',
                options: {
                    context: path.resolve(__dirname, './'),
                    name: "[path][name].[ext]",
                }
            }
        ],
    },
    plugins: [],
};

webpackConfig.plugins.push(
    new webpack.DefinePlugin({ 'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV) })
);

if (process.env.NODE_ENV == 'development') {
    webpackConfig.devtool = 'cheap-module-eval-source-map';
    webpackConfig.output = {
        filename: '[name].js',
        chunkFilename: '[name].js',
        pathinfo: true,
        devtoolModuleFilenameTemplate: info =>
            path.resolve(info.absoluteResourcePath).replace(/\\/g, '/'),
    };
    babelLoader.options = babelLoader.options || {};
    webpackConfig.plugins.push(
        new webpack.NamedModulesPlugin(),
        new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/)
    );
}

if (process.env.NODE_ENV == 'production') {
    webpackConfig.plugins.push(new UglifyJsPlugin({
        parallel: true
    }));
    webpackConfig.devtool = false;
    webpackConfig.output = {
        filename: '[name]-[hash:8].js',
        chunkFilename: '[name]-[hash:8].js'
    };
}

webpackConfig.output.path = path.resolve('dist');

webpackConfig.externals = {
    react: 'React',
    'react-dom': 'ReactDOM',
    'react-router': 'ReactRouter',
    'redux': 'Redux',
    'react-redux': 'ReactRedux',
    jquery: '$',
    'convertlab-uilib': 'CL_uilib',
    'convertlab-ui-common': 'CL_uicommon',
    antd: 'antd',
    moment: 'moment',
    lodash: '_'
};

module.exports = html => {
    webpackConfig.plugins.push(
        new HtmlWebpackPlugin({
            title: html.title,
            template: path.resolve(__dirname, 'template.html'),
            plugin: [...html.plugin || [], ...html.customPlugin || []],
            css: html.css || [],
            env: process.env.NODE_ENV,
            uilibVersion: require('./node_modules/convertlab-uilib/package.json').version,
            uiCommonVersion: require('./node_modules/convertlab-ui-common/package.json').version,
            gitHash: child_process.execSync("git rev-parse --short HEAD").toString().trimRight()
        })
    );
    return webpackConfig;
};
