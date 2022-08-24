<#import "template-vue.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        <#if messageHeader??>
            ${messageHeader}
        <#else>
            ${message.summary}
        </#if>
    <#elseif section = "form">
        <v-card-text>
            ${message.summary}<#if requiredActions??><#list requiredActions>: <b><#items as reqActionItem>${msg("requiredAction.${reqActionItem}")}<#sep>, </#items></b></#list><#else></#if>
        </v-card-text>
        <#if skipLink??>
        <#else>
            <#if pageRedirectUri?has_content>
                <v-card-actions>
                    <v-btn outlined text block color="primary" href="${pageRedirectUri}">
                        ${kcSanitize(msg("backToApplication"))?no_esc}
                    </v-btn>
                </v-card-actions>
            <#elseif actionUri?has_content>
                <v-card-actions>
                    <v-btn outlined text block color="primary" href="${actionUri}">
                        ${kcSanitize(msg("proceedWithAction"))?no_esc}
                    </v-btn>
                </v-card-actions>
            <#elseif (client.baseUrl)?has_content>
                <v-card-actions>
                    <v-btn outlined text block color="primary" href="${client.baseUrl}">
                        ${kcSanitize(msg("backToApplication"))?no_esc}
                    </v-btn>
                </v-card-actions>
            </#if>
        </#if>
    </#if>
</@layout.registrationLayout>