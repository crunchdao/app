FROM node:16.16.0-alpine

RUN mkdir -p /usr/src/nuxt-app
WORKDIR /usr/src/nuxt-app

RUN apk update && apk upgrade
RUN apk update && apk add git

COPY package.json package-lock.json /usr/src/nuxt-app/
RUN npm install

COPY . /usr/src/nuxt-app/
RUN npm run build

EXPOSE 5000

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=5000

CMD [ "npm", "start" ]
