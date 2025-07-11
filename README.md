# Example websocket

# Getting Started

## Java + Spring Boot (API & Websocket) + MySQL 

> [!NOTE]
> Elasticsearch - deprecated - will update it later


```code

CONNECT
accept-version:1.1,1.0
heart-beat:10000,10000

^@


SUBSCRIBE
id:sub-1
destination:/topic/products

^@

SEND
destination:/app/products.save
content-type:application/json

{"name":"iPhone 15","price":"999","description":"IPHONE"}^@

SEND
destination:/app/products.getAll

^@

SEND
destination:/app/products.findById

^@

UNSUBSCRIBE
id:sub-1

^@

```

# System design 
### (Example design - in the simplest way)

* Link to the Chat application - [Official Chat Application](https://phongnghia.io.vn)

![Design](img/img.png)
 
![Technology design system](img/technologies.png)

![UI](img/main-ui.png)

![Login](img/access-ui.png)

![Create and Access to Chat application](img/Create-and-Access.png)

![Notification](img/notification.png)

![Send mail](img/send-mail.png)


> [!NOTE]
> Update system design (Workflow, ...) - Later

# Chat Application in Production

![Access](img/chatapp-access.jpg)

![Create](img/chatapp-create.jpg)

![Verification code](img/chatapp-verification-code.jpg)

![Main app](img/chatapp-main-app.jpg)

![Messages](img/chatapp-message.jpg)

![Mail user code](img/chatapp-mail-user-code.png)

![Mail verification code](img/chatapp-mail-verification-code.png)
