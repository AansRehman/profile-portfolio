import type { Profile } from '../models/resume.model';

export const PROFILE: Profile = {
  name: 'Aans Rehman Khan',
  headline: 'Java Software Engineer @ easypaisa · Tekrowe Digital',
  summary:
    'Software engineer focused on Java Spring Boot, secure APIs, and scalable backends. I work across the SDLC—from design to delivery—with experience in DevOps, MLOps, and Python for ML-style problems. Motivated by new tech, clear engineering, and teams that ship.',
  aboutMe: [
    'Motivated by technology and continuous learning; first in my family to pursue a career in IT.',
    'Problem-solving mindset—comfortable owning challenges end to end and shipping through all SDLC phases.',
    'Strong specialization in Java Spring Boot and the MERN stack, with practical exposure to DevOps, MLOps, and Python.',
    'Enjoy applying Python in data- and ML-oriented projects when the problem calls for it.',
  ],
  whatIBring: [
    'Software engineering: modern web and backend development with Java Spring Boot and MERN, emphasizing secure, scalable applications.',
    'DevOps & MLOps: smoother integration and delivery through repeatable pipelines and automation-minded workflows.',
    'Machine learning: using Python to build practical, intelligent pieces of larger systems.',
  ],
  journeyNotes: [
    'Background that made university and early career a deliberate climb—turned that into drive in software and product work.',
    'Outside work: regular cricket and football; long tours and outdoor challenges when time allows.',
  ],
  connectBlurb:
    'Open to unique projects, new experiences, and conversations with like-minded engineers. Message on LinkedIn or email for collaboration or a casual chat.',
  contact: {
    phone: '+92 318 5428090',
    email: 'aansrehman10@gmail.com',
    github: 'https://github.com/AansRehman',
    linkedin: 'https://www.linkedin.com/in/aans-rehman-khan-229061223',
  },
  education: {
    school: 'National University of Computer and Emerging Sciences (FAST‑NUCES)',
    degree: 'BS Software Engineering',
    location: 'Islamabad, Pakistan',
    period: 'Aug 2020 — Aug 2024',
    courses: [
      'MLOps',
      'Natural Language Processing',
      'Process Mining',
      'Software Design and Analysis',
      'Artificial Intelligence',
      'Blockchain Development',
      'Software Construction and Development',
      'Data Structures',
      'Algorithms',
      'Software Requirement Engineering',
    ],
  },
  priorEducation: [
    {
      school: 'Army Public School (APSACS)',
      credential: 'Intermediate · Computer Science',
      period: 'Mar 2018 — Mar 2020',
    },
    {
      school: 'Army Public School (APSACS)',
      credential: 'Matric · Computer Science',
      period: 'Mar 2016 — Feb 2018',
    },
  ],
  experience: [
    {
      company: 'Tekrowe Digital',
      clientOrNote: 'easypaisa',
      role: 'Java Software Engineer',
      period: 'Apr 2026 — Present',
      bullets: [
        'Collaborating with easypaisa core engineering teams as an on-site vendor resource for fintech solutions.',
        'Contributing to high-traffic financial services and product development within the easypaisa ecosystem.',
        'Managing the full SDLC to ensure seamless integration between vendor services and client platforms.',
      ],
    },
    {
      company: 'Elixir Technologies',
      role: 'Software Engineer',
      period: 'Sep 2025 — Apr 2026',
      bullets: [
        'Developed and maintained RESTful APIs with Spring Boot on the backend team.',
        'Worked across key modules—bug fixes, small features, and learning production architecture and standards.',
        'Partnered with senior engineers on architecture, code quality, and deployment practices.',
      ],
    },
    {
      company: 'Poshmaal Technologies',
      role: 'Java Spring Boot Developer',
      period: 'Oct 2024 — Sep 2025',
      bullets: [
        'Delivered Java Spring Boot work on complex, enterprise-style applications.',
        'Helped translate product ideas into working software within the team.',
        'Explored and adopted newer tooling where it improved delivery and quality.',
      ],
    },
    {
      company: 'Poshmaal Technologies',
      role: 'Intern',
      period: 'Jan 2024 — Oct 2024',
      bullets: [
        'Hands-on experience with Java Spring Boot in complex and enterprise-level applications.',
        'Supported features and fixes while ramping up on team practices and codebase structure.',
      ],
    },
    {
      company: 'Evamp & Saanga',
      role: 'Intern',
      period: 'May 2022 — Jul 2022',
      bullets: [
        'Introduction to Java Spring Boot and building REST APIs with team guidance.',
      ],
    },
  ],
  skillCategories: [
    {
      title: 'Languages',
      items: ['Java', 'React', 'JavaScript', 'Python', 'Git', 'Bash', 'SQL'],
    },
    {
      title: 'Frameworks & stacks',
      items: ['Spring Boot', 'Next.js', 'Quarkus', 'Micronaut', 'Thymeleaf', 'JavaFX', 'MERN Stack'],
    },
    {
      title: 'DevOps & API tools',
      items: ['Git', 'GitHub', 'Docker', 'Jenkins', 'Kubernetes', 'AirFlow', 'Postman', 'Swagger'],
    },
    {
      title: 'Databases',
      items: ['MySQL', 'MySQL Server', 'PostgreSQL', 'MongoDB'],
    },
    {
      title: 'Other',
      items: [
        'Apache Kafka',
        'Spring Security',
        'Hibernate',
        'Maven',
        'Eureka',
        'Load Balancer',
        'GraphQL',
        'API Gateway',
        'Microservices',
        'Design Patterns',
        'Redis',
      ],
    },
  ],
  projects: [
    {
      name: 'Elixir Cloud',
      stack: 'Java 8/17, Spring Boot, React',
      period: 'Sept 2025 — Present',
      bullets: [
        'Built and managed RESTful APIs using Spring Boot, integrating with frontend.',
        'Implemented complex Criteria Queries and optimized database schema using Hibernate ORM for data consistency.',
        'Worked on both backend (Java 8/17) and frontend (React), ensuring seamless integration and maintainable architecture.',
        'Wrote clean, scalable, and secure code following industry best practices.',
      ],
    },
    {
      name: 'Firefly (E-commerce Platform Migration)',
      stack: 'Java, Spring Boot, GraphQL, JWT',
      period: 'Dec 2024 — Aug 2025',
      demoUrl: 'https://prodcheck.firefly.online',
      bullets: [
        'Migrated legacy JavaScript backend to Java Spring Boot, achieving 50% performance improvement.',
        'Implemented secure GraphQL APIs with JWT authentication.',
        'Built modular, production-grade codebase supporting scalability and enhanced maintainability.',
      ],
    },
    {
      name: 'ZoomedXL — Courier Services',
      stack: 'Java, Spring Boot, WebSocket, PostgreSQL',
      period: 'Jan 2025 — Jun 2025',
      bullets: [
        'Developed a RESTful API using Spring Boot to manage courier job creation, assignment, and tracking.',
        'Integrated WebSocket for real-time driver search updates and a bidirectional chat system between customers and drivers.',
        'Integrated WebSocket for real-time chatting between the driver and dispatcher.',
        'Collaborated with front-end developers to ensure seamless integration using Swagger API documentation.',
      ],
    },
  ],
  certifications: [
    {
      title: 'IBM Applied Software Engineering Fundamentals Specialization',
      issuer: 'Coursera',
      date: 'Dec 2023',
    },
    {
      title: 'Building Scalable Java Microservices with Spring Boot and Spring Cloud',
      issuer: 'Google · Coursera',
      date: 'Apr 2023',
    },
    {
      title: 'Python for Data Science, AI & Development',
      issuer: 'IBM · Coursera',
    },
    {
      title: 'Java: Advanced Concepts for High-Performance Development',
      issuer: 'LinkedIn Learning',
    },
    {
      title: 'Getting Started with Git and GitHub',
      issuer: 'Coursera',
    },
    {
      title: 'Hands-on Introduction to Linux Commands and Shell Scripting',
      issuer: 'Coursera',
    },
    {
      title: 'Developing AI Applications with Python and Flask',
      issuer: 'Coursera',
    },
  ],
};
