import axios from "axios";
import API_BASE_URL from "@/config/apiConfig";

// Define Advertisement Interface
export interface Advertisement {
  id: number;
  title: string;
  content: string;
  imageUrl: string;
  targetUrl: string;
  startDate: string;
  endDate: string;
  createdAt: string;
}

// Function to Fetch Active Advertisements
export const fetchAdvertisements = async (): Promise<Advertisement[]> => {
  try {
    console.log(`📡 Fetching advertisements from: ${API_BASE_URL}/advertisements`);
    const response = await axios.get(`${API_BASE_URL}/advertisements`);
    
    // Filter active advertisements based on date
    const now = new Date();
    const activeAds = response.data.filter((ad: Advertisement) => {
      const start = new Date(ad.startDate);
      const end = new Date(ad.endDate);
      return start <= now && now <= end;
    });

    if (activeAds.length === 0) {
      console.warn("⚠️ No active advertisements found.");
    }

    console.log("✅ Fetched Advertisements:", activeAds);
    return activeAds;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.error("❌ Error fetching advertisements:", error.response?.data || error.message);
    } else {
      console.error("❌ Error fetching advertisements:", error);
    }
    throw new Error("Failed to load advertisements");
  }
};
