<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

import ItemForm from '../components/ItemForm.vue'

const items = ref([])

const form = ref({
  id: null,
  code: '',
  name: '',
  price: 0,
  quantity: 0,
  active: true,
  description: '',
})

async function loadItems() {
  const response = await axios.get('http://localhost:8080/api/items')

  items.value = response.data
}

async function saveItem() {
  if (form.value.id) {
    await axios.put(`http://localhost:8080/api/items/${form.value.id}`, form.value)
  } else {
    await axios.post('http://localhost:8080/api/items', form.value)
  }

  resetForm()

  await loadItems()
}

function editItem(item) {
  form.value = {
    ...item,
  }
}

async function deleteItem(id) {
  const confirmed = confirm('Delete this item?')

  if (!confirmed) {
    return
  }

  await axios.delete(`http://localhost:8080/api/items/${id}`)

  await loadItems()
}

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

onMounted(loadItems)
</script>

<template>
  <h1>MiniERP - Master Item</h1>

  <ItemForm :form="form" :isEdit="form.id != null" @save="saveItem" @cancel="resetForm" />

  <hr />

  <table border="1">
    <thead>
      <tr>
        <th>ID</th>
        <th>Code</th>
        <th>Name</th>
        <th>Qty</th>
        <th>Price</th>
        <th>Action</th>
      </tr>
    </thead>

    <tbody>
      <tr v-for="item in items" :key="item.id">
        <td>{{ item.id }}</td>
        <td>{{ item.code }}</td>
        <td>{{ item.name }}</td>
        <td>{{ item.quantity }}</td>
        <td>{{ item.price }}</td>

        <td>
          <button @click="editItem(item)">Edit</button>

          <button @click="deleteItem(item.id)">Delete</button>
        </td>
      </tr>
    </tbody>
  </table>
</template>
