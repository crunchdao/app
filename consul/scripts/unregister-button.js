// ==UserScript==
// @name         Consul Deregister Button
// @version      1
// @description  Add a deregister button to consul since they removed it.
// @author       Enzo CACERES
// @include      http://*:8500/*
// @icon         https://www.google.com/s2/favicons?sz=64&domain=consul.io
// @grant        none
// ==/UserScript==

(function() {
  'use strict';

  const observer = new MutationObserver(() => {
      const element = document.querySelector('section > section > div > header > div > div > div > div > div.copy-button');
      if (!element) {
          return
      }

      // console.log(element)
      if (element.parentElement.childElementCount != 2) {
          return
      }

      let clone = element.cloneNode(true)

      const button = clone.children[0]
      button.innerText = "deregister"
      button.addEventListener('click', async () => {
          button.innerText = "deregistering..."

          const regex = /services\/.+?\/instances\/.+?\/([\w:-]+)/gm;
          const url = window.location.href
          const matcher = regex.exec(url)
          const serviceId = matcher ? matcher[1] : null

          // console.log({ url, regex, matcher, serviceId })

          try {
              // await new Promise(resolve => setTimeout(resolve, 1000));
              const response = await fetch(`/v1/agent/service/deregister/${serviceId}`, {
                  method: 'PUT'
              })

              if (!response.ok) {
                  const text = await response.text()

                  throw Error(`${response.statusText}: ${text}`)
              }
          } catch (error) {
              console.log(error)
              alert(`Could not deregister: ${error}`)
          } finally {
              button.innerText = "deregister"
          }
      })

      clone = element.parentElement.insertBefore(clone, element)
  });

  observer.observe(document.body, {
      childList: true,
      subtree: true
  });
})();
