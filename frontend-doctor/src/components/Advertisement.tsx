"use client";

import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { fetchAdvertisements, Advertisement } from "@/data/advertisements";

const AdvertisementCarousel = () => {
  const [ads, setAds] = useState<Advertisement[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch ads from API
  useEffect(() => {
    fetchAdvertisements()
      .then((data) => {
        if (data.length === 0) {
          setError("No active advertisements available.");
        } else {
          setAds(data);
        }
      })
      .catch(() => setError("Failed to load advertisements."))
      .finally(() => setIsLoading(false));
  }, []);

  // Auto-slide every 5 seconds
  useEffect(() => {
    if (ads.length === 0) return;
    const interval = setInterval(() => {
      nextSlide();
    }, 5000);
    return () => clearInterval(interval);
  }, [ads, currentIndex]);

  // Navigate to next ad
  const nextSlide = () => {
    setCurrentIndex((prevIndex) => (prevIndex + 1) % ads.length);
  };

  // Navigate to previous ad
  const prevSlide = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? ads.length - 1 : prevIndex - 1
    );
  };

  if (isLoading) {
    return <div className="text-center p-6">Loading advertisements...</div>;
  }

  if (error) {
    return <div className="text-center p-6 text-red-500">{error}</div>;
  }

  return (
    <div className="relative w-full max-w-lg mx-auto mt-8">
      {/* Left Navigation Button */}
      <button
        onClick={prevSlide}
        className="absolute left-2 top-1/2 transform -translate-y-1/2 p-2 bg-white shadow-lg rounded-full hover:bg-gray-200 transition-all"
      >
        <ChevronLeft className="h-6 w-6 text-gray-700" />
      </button>

      {/* Advertisement Content */}
      <div className="overflow-hidden relative bg-yellow-200 p-6 rounded-lg shadow-md border-2 border-red-500">
        <AnimatePresence mode="wait">
          <motion.a
            key={ads[currentIndex].id}
            href={ads[currentIndex].targetUrl}
            target="_blank"
            rel="noopener noreferrer"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -50 }}
            transition={{ duration: 0.5 }}
            className="block text-center"
          >
            <img
              src={ads[currentIndex].imageUrl}
              alt={ads[currentIndex].title}
              className="w-full h-40 object-cover rounded-lg mb-4"
            />
            <h2 className="text-xl font-bold mb-2 text-gray-800">
              {ads[currentIndex].title}
            </h2>
            <p className="text-gray-700">{ads[currentIndex].content}</p>
          </motion.a>
        </AnimatePresence>
      </div>

      {/* Right Navigation Button */}
      <button
        onClick={nextSlide}
        className="absolute right-2 top-1/2 transform -translate-y-1/2 p-2 bg-white shadow-lg rounded-full hover:bg-gray-200 transition-all"
      >
        <ChevronRight className="h-6 w-6 text-gray-700" />
      </button>
    </div>
  );
};

export default AdvertisementCarousel;
