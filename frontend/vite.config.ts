import react from '@vitejs/plugin-react';
import type { UserConfig } from 'vite';
import type { InlineConfig } from 'vitest';

interface VitestConfig extends UserConfig {
  test: InlineConfig;
}

const config: VitestConfig = {
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },

  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/setupTests.ts',
  },
};

export default config;
