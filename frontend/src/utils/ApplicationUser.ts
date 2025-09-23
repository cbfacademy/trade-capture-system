export interface ApplicationUser {
  id: number;
  firstName: string;
  lastName: string;
  loginId: string;
  password?: string; // Optional, write-only
  active: boolean;
  userProfile: string; // Profile name/value, not ID
  version: number;
  lastModifiedTimestamp: string; // ISO string
}

