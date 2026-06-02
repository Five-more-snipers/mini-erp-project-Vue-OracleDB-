<!-- ItemView.vue - Master Item CRUD View -->
<!-- 
  Purpose: Main page that displays item list and handles CRUD operations.
  
  Vue.js Concepts demonstrated:
  - Composition API (script setup)
  - Reactivity (ref)
  - Lifecycle hooks (onMounted)
  - Component composition (ItemForm)
  - Event handling (@save, @cancel)
  - Conditional rendering (v-if)
  - List rendering (v-for)
  
  Interview talking points:
  - "I use reactive refs for state management"
  - "Data loading happens in onMounted lifecycle hook"
  - "I separate concerns: view handles UI, api.js handles HTTP calls"
-->

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api'
import ItemForm from '../components/ItemForm.vue'

// Reactive state for the items list
const items = ref([])

// Reactive state for error messages
const error = ref('')

// Reactive state for the form - used for both create and update
const form = ref({
  id: null,
  code: '',
  name: '',
  price: 0,
  quantity: 0,
  active: true,
  description: '',
})

// Fetch all items from backend
async function loadItems() {
  try {
    error.value = ''
    const response = await api.get('/api/items')
    items.value = response.data
  } catch (err) {
    error.value = 'Failed to load items: ' + err.message
  }
}

// Save item - handles both create and update based on form.id
async function saveItem() {
  try {
    error.value = ''
    if (form.value.id) {
      // UPDATE: PUT request with item ID in URL
      await api.put(`/api/items/${form.value.id}`, form.value)
    } else {
      // CREATE: POST request (backend auto-generates code and createdDate)
      await api.post('/api/items', form.value)
    }

    resetForm()
    await loadItems()
  } catch (err) {
    error.value = 'Failed to save item: ' + err.message
  }
}

// Populate form with selected item's data for editing
function editItem(item) {
  form.value = { ...item }
}

// Delete item with confirmation
async function deleteItem(id) {
  const confirmed = confirm('Delete this item?')
  if (!confirmed) return

  try {
    error.value = ''
    await api.delete(`/api/items/${id}`)
    await loadItems()
  } catch (err) {
    error.value = 'Failed to delete item: ' + err.message
  }
}

// Reset form to initial empty state
function resetForm() {
  form.value = {
    id: null,
    code: '',
    name: '',
    price: 0,
    quantity: 0,
    active: true,
    description: '',
  }
}

// Load items when component mounts (equivalent to ngOnInit in Angular, useEffect on mount in React)
onMounted(loadItems)
</script>

<template>
  <div>
    <h1>MiniERP - Master Item</h1>

    <!-- Error message display -->
    <div v-if="error" style="color: red; background: #fee; padding: 10px; margin-bottom: 10px;">
      {{ error }}
    </div>

    <!-- ItemForm component: handles create/update form UI -->
    <!-- :form and :isEdit are props passed down; @save and @cancel are emitted events -->
    <ItemForm :form="form" :isEdit="form.id != null" @save="saveItem" @cancel="resetForm" />

    <hr />

    <!-- Data table displaying all items -->
    <table border="1" style="border-collapse: collapse; width: 100%;">
      <thead>
        <tr>
          <th>ID</th>
          <th>Code</th>
          <th>Name</th>
          <th>Qty</th>
          <th>Price</th>
          <th>Active</th>
          <th>Action</th>
        </tr>
      </thead>

      <tbody>
        <!-- v-for: Vue's list rendering directive. :key is required for list diffing -->
        <tr v-for="item in items" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.code }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.quantity }}</td>
          <td>{{ item.price }}</td>
          <td>{{ item.active ? 'Yes' : 'No' }}</td>

          <td>
            <button @click="editItem(item)">Edit</button>
            <button @click="deleteItem(item.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Empty state when no items exist -->
    <p v-if="items.length === 0" style="color: #666; margin-top: 20px;">
      No items found. Add your first item above.
    </p>
  </div>
</template>
