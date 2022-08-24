<#import "template-vue.ftl" as layout>
<@layout.registrationLayout displayMessage=false; section>
    <#if section = "header">
        ${kcSanitize(msg("errorTitle"))?no_esc}
    <#elseif section = "form">
        <v-card-text class="error--text">
            ${kcSanitize(message.summary)?no_esc}
        </v-card-text>
        <#if skipLink??>
        <#else>
            <#if client?? && client.baseUrl?has_content>
                <v-card-actions>
                    <v-btn outlined text block color="primary" href="${client.baseUrl}">
                        ${kcSanitize(msg("backToApplication"))?no_esc}
                    </v-btn>
                </v-card-actions>
            </#if>
        </#if>
    </#if>
</@layout.registrationLayout>