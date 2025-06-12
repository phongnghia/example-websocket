# DEPLOYMENT WEBSOCKET APPLICATION

# Generate SSL certificates in ZeroSSL

* Use `File upload` -> Download Auth File -> Upload to the server under the `/.well-known/pki-validation/` directory (in `ZeroSSL`)

* Download certificates -> Extract -> Load files to the /etc/ssl directory (Cert pem to `/etc/ssl/certs` and Key pem to `/etc/ssl/private`)

* Upload to certs directory (The mount volume to the container)

* Change permission for the private key

``` code

chmod 400 <key_file_name>.key

```

# Deploy

### Build Backend

```code

docker build -t <registry>/example-websocket:0.1 .

```

### Build Frontend

```code

docker build -t <registry>/example-websocket-frontend:0.1 .

```

### Create deploy, certs, front-end (nginx), backend directories

``` code

mkdir ~/deploy/certs
mkdir -p ~/deploy/backend
mkdir -p ~/deploy/front-end/nginx

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
HOST_EMAIL_ADDRESS=...
HOST_EMAIL_PASSWORD=...

```

### Download certificates from ZeroSSL and extract to ~/deploy/certs

``` code

cp <path>/<certificate_file_name>.zip ~/deploy/certs

or

unzip <certificate_file_name>.zip -d ~/deploy/certs

chmod 400 ~/deploy/<key_file_name>.key

```

### Copy docker compose and Run docker compose

``` code

cp docker-compose.yml ~/deploy

docker compose up -d

```
