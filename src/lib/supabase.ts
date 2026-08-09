/**
 * Layer of service for Supabase integration (TypeScript interface placeholder).
 * In this Android Kotlin environment, the runtime service is handled by com.example.data.remote.SupabaseService.
 */

export const SUPABASE_URL = process.env.VITE_SUPABASE_URL || 'https://your-supabase-project.supabase.co';
export const SUPABASE_PUBLISHABLE_KEY = process.env.VITE_SUPABASE_PUBLISHABLE_KEY || 'your-supabase-anon-key';

export interface SupabaseConfig {
  url: string;
  anonKey: string;
}

export const supabaseConfig: SupabaseConfig = {
  url: SUPABASE_URL,
  anonKey: SUPABASE_PUBLISHABLE_KEY,
};
