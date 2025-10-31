import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

export function setToken(token: string | null) {
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`
  } else {
    delete api.defaults.headers.common['Authorization']
  }
}

export const createShort = (url: string) => api.post('/shorten', { url })
export const myLinks = () => api.get('/my-links')
export const statsByCode = (code: string) => api.get(`/stats/${code}`)
export const deleteByCode = (code: string) => api.delete(`/links/${code}`)

export default api
