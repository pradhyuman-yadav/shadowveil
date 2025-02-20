"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Layout from "@/components/Layout";

export default function EditProfilePage() {
  const router = useRouter();

  // Simulated user data (Replace with real API data)
  const [user, setUser] = useState({
    name: "John Doe",
    email: "johndoe@example.com", // Non-editable email
    profileImage: "/images/profile-placeholder.png",
    role: "Student",
  });

  // Password Change State
  const [showPasswordForm, setShowPasswordForm] = useState(false);
  const [passwords, setPasswords] = useState({
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  });

  const [error, setError] = useState("");
  const [forgotPassword, setForgotPassword] = useState(false); // Triggers reset password option

  // Handle Profile Data Changes
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  // Handle Password Form Toggle
  const togglePasswordForm = () => {
    setShowPasswordForm(!showPasswordForm);
    setError("");
  };

  // Handle Password Input Changes
  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPasswords({ ...passwords, [e.target.name]: e.target.value });
  };

  // Simulated Profile Update Handler
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    // Validate password change only if user entered new password
    if (showPasswordForm) {
      if (!passwords.oldPassword) {
        setError("Please enter your current password.");
        return;
      }
      if (passwords.newPassword.length < 6) {
        setError("New password must be at least 6 characters long.");
        return;
      }
      if (passwords.newPassword !== passwords.confirmPassword) {
        setError("New passwords do not match.");
        return;
      }
      if (passwords.oldPassword !== "correct-password") {
        setError("Incorrect current password.");
        setForgotPassword(true); // Show "Forgot Password?" option
        return;
      }
    }

    console.log("Updated Profile:", user);
    console.log("Updated Password:", showPasswordForm ? passwords.newPassword : "No Change");

    alert("Profile Updated Successfully!");
    router.push("/dashboard"); // Redirect to Dashboard
  };

  // Simulated Password Reset Function
  const handleForgotPassword = () => {
    alert(`A password reset email has been sent to ${user.email}.`);
  };

  return (
    <Layout>
      <div className="max-w-2xl mx-auto bg-white p-6 rounded-lg shadow-md mt-8">
        <h1 className="text-2xl font-bold text-center text-gray-800 mb-6">Edit Profile</h1>

        {error && <p className="text-red-500 text-center">{error}</p>}

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Profile Picture */}
          <div className="flex flex-col items-center">
            <img
              src={user.profileImage}
              alt="Profile"
              className="w-24 h-24 rounded-full shadow-lg border-4 border-gray-200 mb-3"
            />
            <label className="cursor-pointer bg-blue-500 text-white px-4 py-2 rounded-lg text-sm hover:bg-blue-600 transition">
              Change Profile Picture
              <input type="file" className="hidden" />
            </label>
          </div>

          {/* Name */}
          <div>
            <label className="block text-gray-700 font-medium">Full Name</label>
            <input
              type="text"
              name="name"
              value={user.name}
              onChange={handleChange}
              className="w-full border border-gray-300 p-2 rounded-md focus:ring focus:ring-blue-300"
            />
          </div>

          {/* Email (Non-Editable) */}
          <div>
            <label className="block text-gray-700 font-medium">Email Address</label>
            <input
              type="email"
              name="email"
              value={user.email}
              disabled
              className="w-full border border-gray-300 p-2 rounded-md bg-gray-100 cursor-not-allowed"
            />
          </div>

          {/* Role (Read-Only) */}
          <div>
            <label className="block text-gray-700 font-medium">Role</label>
            <input
              type="text"
              name="role"
              value={user.role}
              disabled
              className="w-full border border-gray-300 p-2 rounded-md bg-gray-100 cursor-not-allowed"
            />
          </div>

          {/* Change Password Button */}
          {!showPasswordForm && (
            <button
              type="button"
              onClick={togglePasswordForm}
              className="w-full bg-gray-300 text-gray-800 py-2 rounded-lg font-medium hover:bg-gray-400 transition"
            >
              Change Password
            </button>
          )}

          {/* Password Change Fields (When Form is Shown) */}
          {showPasswordForm && (
            <div className="border-t pt-4">
              <h2 className="text-lg font-semibold text-gray-800 mb-2">Change Password</h2>

              {/* Old Password */}
              <div>
                <label className="block text-gray-700 font-medium">Current Password</label>
                <input
                  type="password"
                  name="oldPassword"
                  value={passwords.oldPassword}
                  onChange={handlePasswordChange}
                  placeholder="Enter current password"
                  className="w-full border border-gray-300 p-2 rounded-md focus:ring focus:ring-blue-300"
                />
              </div>

              {/* New Password */}
              <div>
                <label className="block text-gray-700 font-medium">New Password</label>
                <input
                  type="password"
                  name="newPassword"
                  value={passwords.newPassword}
                  onChange={handlePasswordChange}
                  placeholder="Enter new password"
                  className="w-full border border-gray-300 p-2 rounded-md focus:ring focus:ring-blue-300"
                />
              </div>

              {/* Confirm Password */}
              <div>
                <label className="block text-gray-700 font-medium">Confirm Password</label>
                <input
                  type="password"
                  name="confirmPassword"
                  value={passwords.confirmPassword}
                  onChange={handlePasswordChange}
                  placeholder="Confirm new password"
                  className="w-full border border-gray-300 p-2 rounded-md focus:ring focus:ring-blue-300"
                />
              </div>

              {/* Forgot Password? Link */}
              {forgotPassword && (
                <button
                  type="button"
                  onClick={handleForgotPassword}
                  className="text-blue-500 hover:underline mt-2"
                >
                  Forgot Password?
                </button>
              )}
            </div>
          )}

          {/* Update Profile Button */}
          <button
            type="submit"
            className="w-full bg-blue-500 text-white py-2 rounded-lg font-medium hover:bg-blue-600 transition"
          >
            Update Profile
          </button>

          {/* Cancel Button */}
          <button
            type="button"
            onClick={() => router.push("/dashboard")}
            className="w-full bg-gray-300 text-gray-800 py-2 rounded-lg font-medium hover:bg-gray-400 transition mt-2"
          >
            Cancel
          </button>
        </form>
      </div>
    </Layout>
  );
}
