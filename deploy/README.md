# DEPLOYMENT WEBSOCKET APPLICATION

# Deploy local

### Build Backend

```code

docker build -t phongnghia/example-websocket:0.1 .

```

### Update .env-prod

``` code

MYSQL_ROOT_PASSWORD=...
DB_URL=...
DB_PORT=...
DB_USERNAME=...
DB_PASSWORD=...
CERT_PEM=...
KEY_PEM=...
DOMAIN_NAME=...

```

### Run docker compose

``` code

docker compose up -d

```


# Generate SSL certificates in ZeroSSL

* Use `File upload` -> Download Auth File -> Upload to the server under the `/.well-known/pki-validation/` directory (in `ZeroSSL`)

* Download certificates -> Extract -> Load files to the /etc/ssl directory (Cert pem to `/etc/ssl/certs` and Key pem to `/etc/ssl/private`)

* Upload to certs directory (The mount volume to the container)

* Change permission for the private key

``` code

chmod 400 phongnghia.key

```
