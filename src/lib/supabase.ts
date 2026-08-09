import { createClient } from '@supabase/supabase-js';

export const SUPABASE_URL =
  (typeof process !== 'undefined' && process.env?.VITE_SUPABASE_URL) ||
  'https://your-supabase-project.supabase.co';

export const SUPABASE_PUBLISHABLE_KEY =
  (typeof process !== 'undefined' && process.env?.VITE_SUPABASE_PUBLISHABLE_KEY) ||
  'your-supabase-anon-key';

export const supabaseConfig = {
  url: SUPABASE_URL,
  anonKey: SUPABASE_PUBLISHABLE_KEY,
};

/**
 * Initialized Supabase client instance using VITE_SUPABASE_URL and VITE_SUPABASE_PUBLISHABLE_KEY.
 * Exported for application-wide authentication, data access, and realtime queries.
 */
export const supabase = createClient(SUPABASE_URL, SUPABASE_PUBLISHABLE_KEY);
