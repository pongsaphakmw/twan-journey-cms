import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Emit a standalone server for smaller Docker images
  output: "standalone",
  /* config options here */
};

export default nextConfig;
