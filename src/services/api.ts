import { supabase } from '../lib/supabase';

export interface Profile {
  id?: number | string;
  user_id?: number | string;
  name: string;
  city?: string;
  neighborhood?: string;
  business_name?: string;
  category?: string;
  bio?: string;
  instagram?: string;
  whatsapp?: string;
  email?: string;
  created_at?: string;
}

export interface Opportunity {
  id?: number | string;
  title: string;
  description: string;
  category: string;
  city?: string;
  neighborhood?: string;
  author_id?: number | string;
  author_name?: string;
  author_business?: string;
  author_photo?: string;
  type?: string;
  is_imobiliario?: boolean;
  status?: string;
  created_at?: string;
}

/**
 * Service API providing type-safe CRUD operations for profiles and opportunities using Supabase.
 */
export const profilesApi = {
  async getAll(): Promise<Profile[]> {
    const { data, error } = await supabase
      .from('profiles')
      .select('*');
    if (error) {
      console.error('Error fetching profiles:', error);
      throw error;
    }
    return data || [];
  },

  async getById(id: number | string): Promise<Profile | null> {
    const { data, error } = await supabase
      .from('profiles')
      .select('*')
      .eq('id', id)
      .single();
    if (error) {
      console.error(`Error fetching profile with id ${id}:`, error);
      throw error;
    }
    return data;
  },

  async getByUserId(userId: number | string): Promise<Profile | null> {
    const { data, error } = await supabase
      .from('profiles')
      .select('*')
      .eq('user_id', userId)
      .single();
    if (error) {
      console.error(`Error fetching profile for user_id ${userId}:`, error);
      return null;
    }
    return data;
  },

  async create(profile: Profile): Promise<Profile> {
    const { data, error } = await supabase
      .from('profiles')
      .insert([profile])
      .select()
      .single();
    if (error) {
      console.error('Error creating profile:', error);
      throw error;
    }
    return data;
  },

  async update(id: number | string, profile: Partial<Profile>): Promise<Profile> {
    const { data, error } = await supabase
      .from('profiles')
      .update(profile)
      .eq('id', id)
      .select()
      .single();
    if (error) {
      console.error(`Error updating profile with id ${id}:`, error);
      throw error;
    }
    return data;
  },

  async upsert(profile: Profile): Promise<Profile> {
    const { data, error } = await supabase
      .from('profiles')
      .upsert(profile, { onConflict: 'user_id' })
      .select()
      .single();
    if (error) {
      console.error('Error upserting profile:', error);
      throw error;
    }
    return data;
  },

  async delete(id: number | string): Promise<boolean> {
    const { error } = await supabase
      .from('profiles')
      .delete()
      .eq('id', id);
    if (error) {
      console.error(`Error deleting profile with id ${id}:`, error);
      throw error;
    }
    return true;
  },
};

export const opportunitiesApi = {
  async getAll(): Promise<Opportunity[]> {
    const { data, error } = await supabase
      .from('opportunities')
      .select('*')
      .order('created_at', { ascending: false });
    if (error) {
      console.error('Error fetching opportunities:', error);
      throw error;
    }
    return data || [];
  },

  async getById(id: number | string): Promise<Opportunity | null> {
    const { data, error } = await supabase
      .from('opportunities')
      .select('*')
      .eq('id', id)
      .single();
    if (error) {
      console.error(`Error fetching opportunity with id ${id}:`, error);
      throw error;
    }
    return data;
  },

  async create(opportunity: Opportunity): Promise<Opportunity> {
    const { data, error } = await supabase
      .from('opportunities')
      .insert([opportunity])
      .select()
      .single();
    if (error) {
      console.error('Error creating opportunity:', error);
      throw error;
    }
    return data;
  },

  async update(id: number | string, opportunity: Partial<Opportunity>): Promise<Opportunity> {
    const { data, error } = await supabase
      .from('opportunities')
      .update(opportunity)
      .eq('id', id)
      .select()
      .single();
    if (error) {
      console.error(`Error updating opportunity with id ${id}:`, error);
      throw error;
    }
    return data;
  },

  async delete(id: number | string): Promise<boolean> {
    const { error } = await supabase
      .from('opportunities')
      .delete()
      .eq('id', id);
    if (error) {
      console.error(`Error deleting opportunity with id ${id}:`, error);
      throw error;
    }
    return true;
  },
};
