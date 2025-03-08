/** @type {import('next').NextConfig} */
const nextConfig = {
  ignoreBuildErrors: true,
  eslint: {
    ignoreDuringBuilds: true,
  },
  typescript: {
    ignoreDuringBuilds: true,
  }
}

module.exports = nextConfig 