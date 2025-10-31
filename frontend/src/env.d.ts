interface ImportMetaEnv {
  readonly VITE_PLATFORM_URL?: string;
  readonly VITE_AUTH_URL?: string;
  readonly VITE_PLATFORM_ID?: string;
  readonly VITE_BACKEND_URL?: string;
  // add more env vars here if needed
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
