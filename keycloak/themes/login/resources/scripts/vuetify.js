window.addEventListener('load', () => {
    new Vue({
        el: document.body.children[0],
        vuetify: new Vuetify({
            theme: { dark: true },
        }),
    })
});