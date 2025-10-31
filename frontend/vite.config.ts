import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const backendUrl = env.VITE_BACKEND_URL || 'http://localhost:8080'

  return {
    plugins: [react()],
    server: {
      port: 3001,
      proxy: {
        '/api': backendUrl,
        '/swagger-ui': backendUrl,
        '/api-docs': backendUrl
      }
    }
  }
})
