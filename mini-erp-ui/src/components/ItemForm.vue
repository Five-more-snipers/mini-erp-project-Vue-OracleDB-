<script setup>
defineProps({
  form: Object,
  isEdit: Boolean,
})

const emit = defineEmits(['save', 'cancel'])
</script>

<template>
  <div>
    <input v-model="form.name" placeholder="Name" />
    <input v-model="form.price" placeholder="Price" />

    <!-- <input v-model="form.quantity" placeholder="Quantity" /> -->

    <button @click="emit('save')">
      {{ isEdit ? 'Update' : 'Save' }}
    </button>

    <button @click="emit('cancel')">Cancel</button>
  </div>
</template>
