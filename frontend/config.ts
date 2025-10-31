export const CONFIG = {
  platformUrl: import.meta.env.VITE_PLATFORM_URL || "http://localhost:3000",
  authUrl: import.meta.env.VITE_AUTH_URL || "https://auth.dev.hr.alabuga.space",
  platformId: import.meta.env.VITE_PLATFORM_ID || "alb-start",
  backendUrl: import.meta.env.VITE_BACKEND_URL || "http://localhost:8080",
} as const;
