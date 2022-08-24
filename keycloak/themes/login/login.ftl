<#import "template-vue.ftl" as layout>
<@layout.registrationLayout displayMessage=!messagesPerField.existsError('username','password') displayInfo=false; section>
    <#if section = "header">
        ${msg("loginAccountTitle")}
    <#elseif section = "form">
        <v-card-text>
            <v-form id="form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                <#if !usernameHidden??>
                    <v-text-field
                        id="username"
                        label="<#if !realm.loginWithEmailAllowed>${msg('username')}<#elseif !realm.registrationEmailAsUsername>${msg('usernameOrEmail')}<#else>${msg('email')}</#if>"
                        outlined
                        name="username"
                        value="${(login.username!'')}"
                        type="text"
                        autofocus
                        autocomplete="off"
                        prepend-icon="mdi-email"
                        dense
                        <#if messagesPerField.existsError('username','password')>
                            error
                            error-messages="${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}"
                        </#if>
                    ></v-text-field>
                </#if>

                <v-text-field
                    id="password"
                    label="${msg('password')}"
                    outlined
                    name="password"
                    type="password"
                    autocomplete="off"
                    hide-details="auto"
                    prepend-icon="mdi-lock"
                    dense
                    <#if messagesPerField.existsError('username','password')>
                        error
                        <#if usernameHidden??>
                            error-messages="${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}"
                        </#if>
                    </#if>
                ></v-text-field>
                <input type="hidden" id="id-hidden-input" name="credentialId" <#if auth.selectedCredential?has_content>value="${auth.selectedCredential}"</#if>/>
            </v-form>
        </v-card-text>
        <v-card-actions>
            <v-btn color="primary" block name="login" id="login" type="submit" form="form-login">
                ${msg("doLogIn")}
            </v-btn>
        </v-card-actions>
        <v-card-actions class="pt-0">
            <v-btn outlined text block color="primary" href="${url.loginResetCredentialsUrl}">
                ${msg("doForgotPassword")}
            </v-btn>
        </v-card-actions>
    </#if>

</@layout.registrationLayout>