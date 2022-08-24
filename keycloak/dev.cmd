docker run ^
    --name keycloak-dev ^
    --rm ^
    -it ^
    -p 8080:8080 ^
    -e KEYCLOAK_ADMIN=admin ^
    -e KEYCLOAK_ADMIN_PASSWORD=admin ^
    -v %cd%/themes:/opt/keycloak/themes/custom ^
    quay.io/keycloak/keycloak ^
    start-dev
