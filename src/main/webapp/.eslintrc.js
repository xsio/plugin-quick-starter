const eslintrc = {
    env: {
        browser: true,
        es6: true,
        es2017: true,
        jquery: true
    },
    parser: 'babel-eslint',
    plugins: ['babel'],
    rules: {
        'object-curly-spacing': ["warn", "always"]
    },
};

module.exports = eslintrc;