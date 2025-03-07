import { motion } from 'framer-motion';

interface PageHeaderProps {
  title: string;
  subtitle?: string;
  rightContent?: React.ReactNode;
}

export default function PageHeader({ title, subtitle, rightContent }: PageHeaderProps) {
  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="bg-gradient-to-b from-neutral-800 to-neutral-900"
    >
      <div className="max-w-4xl mx-auto px-4 py-8">
        <motion.div 
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center"
        >
          <motion.h1 
            className="text-3xl font-bold text-white mb-3"
            whileHover={{ scale: 1.02 }}
            transition={{ type: "spring", stiffness: 300 }}
          >
            {title}
          </motion.h1>
          {subtitle && (
            <motion.p 
              className="text-neutral-400 mb-4"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.2 }}
            >
              {subtitle}
            </motion.p>
          )}
          {rightContent && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              whileHover={{ 
                scale: 1.05,
                backgroundColor: "rgba(59, 130, 246, 0.1)" // blue-600 with opacity
              }}
              whileTap={{ scale: 0.95 }}
              transition={{ 
                duration: 0.2,
                type: "spring",
                stiffness: 300
              }}
              className="inline-block px-4 py-2 bg-neutral-800 rounded-full text-sm text-neutral-400 cursor-pointer"
            >
              {rightContent}
            </motion.div>
          )}
        </motion.div>
      </div>
    </motion.div>
  );
} 