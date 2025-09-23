import React, { useEffect, useState } from 'react';
import Button from '../components/Button';
import Input from '../components/Input';
import Dropdown from '../components/Dropdown';
import Snackbar from '../components/Snackbar';
import { createUser, fetchUserProfiles } from '../utils/api';

interface SignUpProps {
  isOpen: boolean;
  onClose: () => void;
}

const SignUp: React.FC<SignUpProps> = ({ isOpen, onClose }) => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [loginId, setLoginId] = useState('');
  const [password, setPassword] = useState('');
  const [userProfile, setUserProfile] = useState('');
  const [profiles, setProfiles] = useState<{ id: number; userType: string }[]>([]);
  const [snackOpen, setSnackOpen] = useState(false);
  const [snackMessage, setSnackMessage] = useState('');
  const [snackType, setSnackType] = useState<'success' | 'error'>('success');

  useEffect(() => {
    if (!isOpen) return;
    fetchUserProfiles()
      .then((res) => setProfiles(res.data))
      .catch(() => setProfiles([]));
  }, [isOpen]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await createUser({ firstName, lastName, loginId, password, userProfile });
      setSnackType('success');
      setSnackMessage('User created successfully');
      setFirstName('');
      setLastName('');
      setLoginId('');
      setPassword('');
      setUserProfile('');
      onClose();
    } catch (error) {
      setSnackType('error');
      setSnackMessage(error instanceof Error ? error.message : 'Failed to create user');
    } finally {
      setSnackOpen(true);
      setTimeout(() => setSnackOpen(false), 3000);
    }
  };

  if (!isOpen) return null;

  const options = profiles.map((p) => ({ value: p.userType, label: p.userType }));

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
        <h1 className="text-2xl font-bold mb-4">Sign Up</h1>
        <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
          <Input label="First Name" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
          <Input label="Last Name" value={lastName} onChange={(e) => setLastName(e.target.value)} />
          <Input label="Login Id" value={loginId} onChange={(e) => setLoginId(e.target.value)} />
          <Input
            label="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Dropdown
            value={userProfile}
            onChange={(e) => setUserProfile(e.target.value)}
            options={options}
            placeholder="Select profile"
          />
          <div className="flex justify-end gap-2 mt-2">
            <Button type="submit" variant="primary">
              Sign Up
            </Button>
            <Button type="button" variant="secondary" onClick={onClose}>
              Cancel
            </Button>
          </div>
        </form>
      </div>
      <Snackbar open={snackOpen} message={snackMessage} type={snackType} onClose={() => setSnackOpen(false)} />
    </div>
  );
};

export default SignUp;
