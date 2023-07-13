import { useState } from 'react';

import { useTranslation } from 'next-i18next';

import { useFetch } from '@/hooks/useFetch';

import axios from 'axios';
import { url } from 'inspector';

export const RegisterDialog = () => {
  const { t } = useTranslation('chat');

  const [error, setError] = useState<string | null>(null);

  const [username, setUsername] = useState<string | null>(null);
  const [password, setPassword] = useState<string | null>(null);

  const handleRegister = async () => {
    try {
      const response = await axios.post('/register', {
        username: username,
        password: password,
      });
      setError('Register success. Please login');
    } catch (err) {
      setError('Register failed. Please try again');
    }
  };

  return (
    <>
      <div className="mx-auto flex flex-col space-y-5 md:space-y-10 px-3 pt-5 md:pt-12 sm:max-w-[600px]">
        <div className="text-center text-3xl font-semibold text-gray-800 dark:text-gray-100">
          Register
        </div>

        {
          <div className="flex h-full flex-col space-y-4 rounded-lg border border-neutral-200 p-4 dark:border-neutral-600">
            <div className="flex flex-col">
              <div className="w-full rounded-lg border border-neutral-200 bg-transparent pr-2 text-neutral-900 dark:border-neutral-600 dark:text-white">
                <input
                  type="text"
                  className="w-full bg-transparent p-2"
                  placeholder="Username"
                  onChange={(e) => {
                    setUsername(e.target.value);
                  }}
                ></input>
              </div>
            </div>

            <div className="flex flex-col">
              <div className="w-full rounded-lg border border-neutral-200 bg-transparent pr-2 text-neutral-900 dark:border-neutral-600 dark:text-white">
                <input
                  type="password"
                  className="w-full bg-transparent p-2"
                  placeholder="Password"
                  onChange={(e) => {
                    setPassword(e.target.value);
                  }}
                ></input>
              </div>
            </div>

            {/* <div className="flex flex-col">
              <div className="w-full rounded-lg border border-neutral-200 bg-transparent pr-2 text-neutral-900 dark:border-neutral-600 dark:text-white">
                <input
                  type="password"
                  className="w-full bg-transparent p-2"
                  placeholder="Confirm password"
                ></input>
              </div>
            </div> */}

            {error && <div className="flex flex-col pt-2">{error}</div>}

            <div className="flex flex-col">
              <div className="w-full rounded-lg bg-transparent  text-neutral-900 dark:border-neutral-600 dark:text-white">
                <button
                  type="button"
                  className="w-full px-4 mt-8 py-2 border rounded-lg shadow border-neutral-500 text-neutral-900 hover:bg-neutral-100 focus:outline-none dark:border-neutral-800 dark:border-opacity-50 dark:bg-white dark:text-black dark:hover:bg-neutral-300"
                  onClick={handleRegister}
                >
                  {t('Register')}
                </button>
              </div>
            </div>
          </div>
        }
      </div>
    </>
  );
};
