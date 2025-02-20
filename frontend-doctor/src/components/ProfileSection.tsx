"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

const ProfileSection = () => {
  const router = useRouter();

  // Simulated user data (Replace with API data)
  const [user, setUser] = useState({
    name: "John Doe",
    email: "johndoe@example.com",
    role: "Student",
    profileImage: "/images/profile-placeholder.png", // Placeholder image
    accountType: "Premium",
    membershipStatus: "Active",
    accountCreated: "January 15, 2023",
    lastLogin: "February 15, 2025",
  });

  // Simulated Logout Function
  const handleLogout = () => {
    alert("Logging out...");
    router.push("/login"); // Redirect to login page
  };

  return (
    <div className="flex flex-col items-center bg-gradient-to-r from-blue-500 to-indigo-500 text-white p-6 rounded-lg shadow-xl w-full">
      {/* Profile Picture */}
      <div className="relative group">
        <img
          src={user.profileImage}
          alt="Profile"
          className="w-24 h-24 rounded-full shadow-lg border-4 border-white transition-transform transform group-hover:scale-110 cursor-pointer"
        />
        <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 bg-black bg-opacity-50 rounded-full">
          <span className="text-white text-xs">Change</span>
        </div>
      </div>

      {/* User Details */}
      <h2 className="text-lg font-bold mt-3">{user.name}</h2>
      <p className="text-sm text-gray-200">{user.email}</p>
      <span className="bg-white text-blue-600 px-3 py-1 rounded-full text-xs mt-2">
        {user.role}
      </span>

      {/* Additional Profile Information */}
      <div className="mt-4 w-full text-center">
        <p className="border-b border-white pb-1">
          <strong>Account Type:</strong> {user.accountType}
        </p>
        <p className="border-b border-white pb-1 mt-1">
          <strong>Membership:</strong> {user.membershipStatus}
        </p>
        <p className="border-b border-white pb-1 mt-1">
          <strong>Account Created:</strong> {user.accountCreated}
        </p>
        <p className="mt-1">
          <strong>Last Login:</strong> {user.lastLogin}
        </p>
      </div>

      {/* Profile Actions */}
      <div className="mt-6 flex flex-col space-y-3 w-full">
        <Link href="/dashboard/profile/edit">
          <button className="bg-white text-blue-600 font-semibold py-2 px-4 rounded-lg hover:bg-gray-200 transition w-full">
            Edit Profile
          </button>
        </Link>
        <button className="bg-gray-300 text-gray-800 font-semibold py-2 px-4 rounded-lg hover:bg-gray-400 transition w-full">
          Change Password
        </button>
        <button
          onClick={handleLogout}
          className="bg-red-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-red-600 transition w-full"
        >
          Logout
        </button>
      </div>
    </div>
  );
};

export default ProfileSection;
