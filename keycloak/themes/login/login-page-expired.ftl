<#import "template-vue.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "header">
        ${msg("pageExpiredTitle")}
    <#elseif section = "form">
        <v-card-text>
            ${msg("pageExpiredMsg1")} <a id="loginRestartLink" href="${url.loginRestartFlowUrl}">${msg("doClickHere")}</a>.<br/>
            ${msg("pageExpiredMsg2")} <a id="loginContinueLink" href="${url.loginAction}">${msg("doClickHere")}</a>.
        </v-card-text>
    </#if>
</@layout.registrationLayout>
