docker run ^
    --rm ^
    -it ^
    -e KEYCLOAK_ADMIN=admin ^
    -e KEYCLOAK_ADMIN_PASSWORD=admin ^
    -v %cd%/themes:/opt/keycloak/themes/custom ^
    --entrypoint "" ^
    keycloak ^
    bash -c "cd /opt/keycloak/themes/ && find custom -maxdepth 3"
