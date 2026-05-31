import axios from 'axios'
import api from '@/services/api'

export default axios.create({
  baseURL: 'http://localhost:8080'
})
await api.get('/api/items')