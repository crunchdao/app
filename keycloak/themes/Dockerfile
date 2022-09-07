FROM node:16-alpine
WORKDIR /data/
COPY login/resources/package.json .
RUN npm install

FROM quay.io/keycloak/keycloak:latest
ENV THEME_DIRECTORY=/opt/keycloak/themes/custom/
COPY login/ $THEME_DIRECTORY/login/
COPY --from=0 /data/ $THEME_DIRECTORY/resources

ENV KC_LOG_LEVEL=INFO
ENV KC_PROXY=edge
ENV KC_HTTP_ENABLED=true
ENV KC_HOSTNAME_STRICT=false
ENV KC_HOSTNAME_STRICT_HTTPS=false

CMD ["start"]