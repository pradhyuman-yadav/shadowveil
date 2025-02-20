"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";
import Navbar from "@/components/Navbar";
import API_BASE_URL from "@/config/apiConfig";

export default function SignUpPage() {
  const router = useRouter();

  // Form States
  const [username, setUsername] = useState("");
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [institution, setInstitution] = useState("");
  const [customInstitution, setCustomInstitution] = useState("");
  const [role, setRole] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  // Predefined Institutions (Can be fetched from an API)
  const institutions = ["Harvard University", "Stanford University", "MIT", "Other"];

  const handleSignUp = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    // Trim inputs to prevent empty spaces
    const trimmedUsername = username.trim();
    const trimmedName = name.trim();
    const trimmedEmail = email.trim();
    const trimmedPassword = password.trim();
    const trimmedConfirmPassword = confirmPassword.trim();
    const finalInstitution = institution === "Other" ? customInstitution.trim() : institution.trim();

    // ✅ Validate required fields
    if (!trimmedUsername || !trimmedName || !trimmedEmail || !finalInstitution || !role || !trimmedPassword || !trimmedConfirmPassword) {
      setError("All fields are required.");
      setLoading(false);
      return;
    }

    // ✅ Ensure passwords match
    if (trimmedPassword !== trimmedConfirmPassword) {
      setError("Passwords do not match.");
      setLoading(false);
      return;
    }

    // ✅ API Payload for User Registration
    const payload = {
      username: trimmedUsername,
      email: trimmedEmail,
      password: trimmedPassword,
      role,
    };

    try {
      // ✅ Send sign-up request
      const response = await axios.post("API_BASE_URL/api/signup", payload);

      if (response.status === 201) {
        // ✅ Send email verification request
        await axios.post("API_BASE_URL/api/send-verification-email", { email: trimmedEmail });

        setSuccess(true);

        // ✅ Redirect to login page after 3 seconds
        setTimeout(() => {
          router.push("/login");
        }, 3000);
      }
    } catch (err: any) {
      setError(err.response?.data?.message || "Registration failed. Try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Navbar />

      <div className="flex items-center justify-center min-h-screen bg-gray-100 pt-16">
        <div className="w-full max-w-md bg-white p-6 rounded-lg shadow-md">
          <h1 className="text-2xl font-bold text-center mb-6">Sign Up</h1>

          {success ? (
            <div className="text-center text-green-600 font-medium">
              ✅ Sign-up successful! Check your email for verification.
            </div>
          ) : (
            <form onSubmit={handleSignUp} className="space-y-4">
              {/* Username Field */}
              <div>
                <label className="block text-sm font-medium text-gray-700">Username</label>
                <input
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                  className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                  placeholder="your_username"
                />
              </div>

              {/* Name Field */}
              <div>
                <label className="block text-sm font-medium text-gray-700">Full Name</label>
                <input
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                  className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                  placeholder="John Doe"
                />
              </div>

              {/* Email Field */}
              <div>
                <label className="block text-sm font-medium text-gray-700">Email Address</label>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                  placeholder="you@example.com"
                />
              </div>

              {/* Institution Dropdown */}
              <div>
                <label className="block text-sm font-medium text-gray-700">Institution</label>
                <select
                  value={institution}
                  onChange={(e) => setInstitution(e.target.value)}
                  required
                  className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                >
                  <option value="">Select Institution</option>
                  {institutions.map((inst) => (
                    <option key={inst} value={inst}>
                      {inst}
                    </option>
                  ))}
                </select>
              </div>

              {/* Custom Institution (Only If "Other" Selected) */}
              {institution === "Other" && (
                <div>
                  <label className="block text-sm font-medium text-gray-700">Enter Your Institution</label>
                  <input
                    type="text"
                    value={customInstitution}
                    onChange={(e) => setCustomInstitution(e.target.value)}
                    required
                    className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                    placeholder="Your Institution Name"
                  />
                </div>
              )}

              {/* Role Selection */}
              <div>
                <label className="block text-sm font-medium text-gray-700">Role</label>
                <div className="flex space-x-4">
                  <button
                    type="button"
                    onClick={() => setRole("Student")}
                    className={`w-1/2 py-2 px-4 rounded-md text-center border ${
                      role === "Student"
                        ? "bg-blue-500 text-white"
                        : "border-gray-300 text-gray-700"
                    }`}
                  >
                    Student
                  </button>
                  <button
                    type="button"
                    onClick={() => setRole("Instructor")}
                    className={`w-1/2 py-2 px-4 rounded-md text-center border ${
                      role === "Instructor"
                        ? "bg-blue-500 text-white"
                        : "border-gray-300 text-gray-700"
                    }`}
                  >
                    Instructor
                  </button>
                </div>
              </div>

              {/* Password & Confirm Password */}
              <div>
                <label className="block text-sm font-medium text-gray-700">Password</label>
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                  placeholder="••••••••"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Confirm Password</label>
                <input
                  type="password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  required
                  className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
                  placeholder="••••••••"
                />
              </div>

              {/* Error Message */}
              {error && <p className="text-red-500 text-sm">{error}</p>}

              {/* Sign Up Button */}
              <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-md" disabled={loading}>
                {loading ? "Signing Up..." : "Sign Up"}
              </button>
            </form>
          )}
        </div>
      </div>
    </>
  );
}
