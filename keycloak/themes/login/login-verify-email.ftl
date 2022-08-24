<#import "template-vue.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        ${msg("emailVerifyTitle")}
    <#elseif section = "form">
        <v-card-text>
            ${msg("emailVerifyInstruction1",user.email)}
        </v-card-text>
    <#elseif section = "info">
        <v-card-subtitle>
            ${msg("emailVerifyInstruction2")}
        </v-card-subtitle>
        <v-card-actions>
            <v-btn outlined text block color="primary" href="${url.loginAction}">
                ${msg("doClickHere")} ${msg("emailVerifyInstruction3")}
            </v-btn>
        </v-card-actions>
    </#if>
</@layout.registrationLayout>
