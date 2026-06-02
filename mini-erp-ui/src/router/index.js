// router/index.js - Vue Router Configuration
// Purpose: Maps URLs to Vue components. This is how SPA (Single Page App) navigation works.
// Interview point: "I use Vue Router for client-side routing, enabling navigation without full page reloads."

import { createRouter, createWebHistory } from 'vue-router'
import ItemView from '../views/ItemView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'items',
      component: ItemView,
    },
  ],
})

export default router
