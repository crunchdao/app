<#macro registrationLayout bodyClass="" displayInfo=false displayMessage=true displayRequiredFields=false>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="robots" content="noindex, nofollow">

    <#if properties.meta?has_content>
        <#list properties.meta?split(' ') as meta>
            <meta name="${meta?split('==')[0]}" content="${meta?split('==')[1]}"/>
        </#list>
    </#if>
    <title>${msg("loginTitle",(realm.displayName!''))}</title>
    <link rel="icon" href="${url.resourcesPath}/img/favicon.ico" />
    <#if properties.stylesCommon?has_content>
        <#list properties.stylesCommon?split(' ') as style>
            <link href="${url.resourcesCommonPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.styles?has_content>
        <#list properties.styles?split(' ') as style>
            <link href="${url.resourcesPath}/${style}" rel="stylesheet" />
        </#list>
    </#if>
    <#if properties.scripts?has_content>
        <#list properties.scripts?split(' ') as script>
            <script src="${url.resourcesPath}/${script}" type="text/javascript"></script>
        </#list>
    </#if>
    <#if scripts??>
        <#list scripts as script>
            <script src="${script}" type="text/javascript"></script>
        </#list>
    </#if>
</head>

<body>
    <v-app>
        <v-layout align-center justify-center column>
            <div class="mb-4">
                <v-img src="${url.resourcesPath}/images/logo.png"></v-img>
            </div>
            <#if displayMessage && message?has_content && (message.type != 'warning' || !isAppInitiatedAction??)>
                <v-alert type="${message.type}" class="mx-4" max-width="800" prominent>
                    ${kcSanitize(message.summary)?no_esc}
                </v-alert>
            </#if>
            <v-card width="400" max-width="600">
                <#if !(auth?has_content && auth.showUsername() && !auth.showResetCredentials())>
                    <v-card-title><#nested "header"></v-card-title>
                <#else>
                    <v-card-title>
                        ${auth.attemptedUsername}
                        <v-spacer></v-spacer>
                        <v-btn text outlined color="primary" href="${url.loginRestartFlowUrl}">
                            <v-icon left>mdi-restart</v-icon>
                            ${msg("restartLoginTooltip")}
                        </v-btn>
                    </v-card-title>
                    <#nested "show-username">
                </#if>
                <#if displayInfo>
                    <#nested "info">
                </#if>
                <#nested "form">
            </v-card>
        </v-layout>
    </v-app>
</body>
</html>
</#macro>
