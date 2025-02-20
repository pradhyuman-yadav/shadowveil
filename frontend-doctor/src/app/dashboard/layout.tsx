import Navbar from "@/components/Navbar";
import ProfileSection from "@/components/ProfileSection";

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar - User Profile */}
      <aside className="w-64 bg-white shadow-lg p-6 flex flex-col">
        <ProfileSection />
      </aside>

      {/* Main Content Area */}
      <div className="flex flex-col flex-grow">
        {/* Navbar */}
        <Navbar />

        {/* Page Content */}
        <main className="p-6 bg-gray-50 flex-grow overflow-y-auto">{children}</main>
      </div>
    </div>
  );
}
