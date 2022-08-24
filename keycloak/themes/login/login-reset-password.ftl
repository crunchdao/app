<#import "template-vue.ftl" as layout>
<@layout.registrationLayout displayInfo=true displayMessage=!messagesPerField.existsError('username'); section>
    <#if section = "header">
        ${msg("emailForgotTitle")}
    <#elseif section = "form">
        <v-card-text>
            <v-form id="reset-password-form" action="${url.loginAction}" method="post">
                <v-text-field
                    id="username"
                    label="<#if !realm.loginWithEmailAllowed>${msg('username')}<#elseif !realm.registrationEmailAsUsername>${msg('usernameOrEmail')}<#else>${msg('email')}</#if>"
                    outlined
                    type="text"
                    name="username"
                    autofocus
                    value="${(auth.attemptedUsername!'')}"
                    hide-details="auto"
                    prepend-icon="mdi-email"
                    dense
                    <#if messagesPerField.existsError('username')>
                        error
                        error-messages="${kcSanitize(messagesPerField.get('username'))?no_esc}"
                    </#if>
                ></v-text-field>
            </v-form>
        </v-card-text>
        <v-card-actions>
            <v-btn color="primary" block type="submit" form="reset-password-form">
                ${msg("doSubmit")}
            </v-btn>
        </v-card-actions>
        <v-card-actions class="pt-0">
            <v-btn outlined text block color="primary" href="${url.loginUrl}">
                ${kcSanitize(msg("backToLogin"))?no_esc}
            </v-btn>
        </v-card-actions>
    <#elseif section = "info" >
        <v-card-subtitle>
            <#if realm.duplicateEmailsAllowed>
                ${msg("emailInstructionUsername")}
            <#else>
                ${msg("emailInstruction")}
            </#if>
        </v-card-subtitle>
    </#if>
</@layout.registrationLayout>
