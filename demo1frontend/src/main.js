import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
import './assets/css/icon.css'
const app = createApp(App)
import qs from "qs";
import {AIMarker} from "vue-picture-bd-marker"
installElementPlus(app)
app
    .use(store)
    .use(router)
    .mount('#app')
