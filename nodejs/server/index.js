const express = require('express');
const proxy = require('http-proxy-middleware');
const path = require('path');
const crypto = require('crypto');

const app = express();
const port = process.env.PORT || 8081;

function makeSignature(method, url, timestamp, accessKey, secretKey) {
  const hmac = crypto.createHmac('sha256', secretKey);
  const message = `${method} ${url}\n${timestamp}\n${accessKey}`;
  hmac.update(message);
  hmac.end();

  return hmac.read().toString('base64');
}

const options = {
  target: 'https://cloudsearch.apigw.ntruss.com',
  changeOrigin: true,
  secure: true,
  pathRewrite: {
    '^/api': '/CloudSearch/real/v1'
  },
  onProxyReq : (proxyReq, req, res) => {
    const primaryKey = {primaryKey};
    const accessKey = {accessKey};
    const secretKey = {secretKey};
    const timestamp = new Date().getTime();

    proxyReq.setHeader('x-ncp-apigw-timestamp', timestamp);
    proxyReq.setHeader('x-ncp-apigw-api-key', primaryKey);
    proxyReq.setHeader('x-ncp-iam-access-key', accessKey);

    const method = req.method;
    const url = req.url;
    const message = makeSignature(method, url, timestamp, accessKey, secretKey);

    proxyReq.setHeader('x-ncp-apigw-signature-v2', message);

    console.log(`${method} : ${url}`);
  },
};

app.use('/api', proxy(options));

if (process.env.NODE_ENV === 'production') {
  app.use(express.static(path.join(__dirname, '../build')));

  app.get('/', function (req, res) {
    res.sendFile(path.join(__dirname, '../build', 'index.html'));
  });
}
app.listen(port, () => console.log(`Listening on port ${port}`));
