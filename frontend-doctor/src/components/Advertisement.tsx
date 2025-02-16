import React from "react";

const Advertisement = () => {
  return (
    <div className="bg-yellow-200 p-4 rounded-lg shadow-md mt-8 border-2 border-red-500">
      <h2 className="text-xl font-bold mb-4 text-gray-800">Special Offer!</h2>
      <p className="text-gray-700 mb-4">
        Get 50% off on our premium video courses. Limited time offer!
      </p>
      <a
        href="/premium-courses"
        className="block bg-hospitalBlue text-white text-center py-2 rounded-lg hover:bg-blue-700 transition-colors"
      >
        Upgrade Now
      </a>
    </div>
  );
};

export default Advertisement;
