export interface ApplicationUser {
  id: number;
  firstName: string;
  lastName: string;
  loginId: string;
  password?: string;
  active: boolean;
  userProfile: string;
  version: number;
  lastModifiedTimestamp: string;
}

