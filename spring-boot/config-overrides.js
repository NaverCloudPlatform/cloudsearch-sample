const path = require('path');


module.exports = {
    paths: function (paths, env) {
        paths.appPublic = path.resolve(__dirname, 'src/main/public');
        paths.appHtml = path.resolve(__dirname, 'src/main/public/index.html');
        paths.appIndexJs = path.resolve(__dirname, 'src/main/app/index.tsx');
        paths.appSrc = path.resolve(__dirname, 'src/main/app');
        paths.testsSetup = path.resolve(__dirname, 'src/main/app/setupTests.ts');

        return paths;
    },
}
