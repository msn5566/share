# share

graph TD
    subgraph Client_Side ["🖥️ Client Side (Browser)"]
        UI[React Frontend (Tailwind CSS)]
        Service[GeminiService (API Client)]
        UI -->|Calls| Service
    end

    subgraph Backend_Server ["⚙️ Backend Node.js Server"]
        API[Express API / Middleware]
        Auth[Auth Handler]
        
        subgraph Agents ["🤖 AI Agent Layer"]
            Validator[Validation Agent<br/>(Safety & Logic Check)]
            Vision[Vision Agent<br/>(Image Analysis)]
            Generator[Content Generator<br/>(Prompt Engineering)]
        end

        API --> Auth
        Auth --> Validator
        Validator --> Generator
        Auth --> Vision
    end

    subgraph Data_Layer ["📂 Local Data Persistence"]
        FS[(File System /data)]
        Users[users.json]
        UserDir[User Directory]
        Policies[policies/ (.txt, .json)]
        History[history.json]
        
        FS -- contains --> Users
        FS -- contains --> UserDir
        UserDir -- contains --> Policies
        UserDir -- contains --> History
    end

    subgraph External_Services ["☁️ External Cloud Services"]
        Gemini[Google Gemini 2.5 API]
        OpenAI[OpenAI GPT-4o API]
        Shopify[Shopify Store API<br/>(Simulated)]
    end

    %% Connections
    Service -->|HTTP JSON| API
    
    Generator -->|Reads Context| Policies
    API -->|Reads/Writes| History
    API -->|Reads| Users
    
    Validator -->|API Call| Gemini
    Validator -->|API Call| OpenAI
    Vision -->|API Call| Gemini
    Vision -->|API Call| OpenAI
    Generator -->|API Call| Gemini
    Generator -->|API Call| OpenAI
    
    API -->|Publish| Shopify

    style Backend_Server fill:#f9f,stroke:#333,stroke-width:2px
    style Data_Layer fill:#ff9,stroke:#333,stroke-width:2px
    style Agents fill:#bbf,stroke:#333,stroke-width:2px





sequenceDiagram
    actor User
    participant FE as Frontend (React)
    participant BE as Backend (Express)
    participant DB as File System (Data)
    participant AI as AI Model (Gemini/OpenAI)

    Note over User, AI: Scenario: User Generates Product Content

    User->>FE: Inputs Product Data + Image
    User->>FE: Clicks "Generate"
    FE->>BE: POST /api/generate (with Token)
    
    rect rgb(240, 248, 255)
        Note right of BE: Step 1: Context Loading
        BE->>DB: Load User Policies (blocked_terms.txt, brand_guide.pdf)
        DB-->>BE: Return Policy Content
    end

    rect rgb(255, 230, 230)
        Note right of BE: Step 2: Safety Guardrail
        BE->>AI: VALIDATION AGENT PROMPT<br/>(Check for illegal items, safety, logic mismatch)
        AI-->>BE: { "valid": true, "reason": null }
    end

    alt Input is Invalid
        BE-->>FE: Error 400: Validation Failed
        FE->>User: Show Alert Modal
    else Input is Valid
        rect rgb(230, 255, 230)
            Note right of BE: Step 3: Content Generation
            BE->>BE: Inject Policies into System Instruction
            BE->>BE: Inject SEO Keywords & Customer Reviews
            BE->>AI: GENERATION AGENT PROMPT<br/>(Create Title, Desc, Bullets, SEO)
            AI-->>BE: JSON Response
        end
        
        BE->>DB: Save to history.json
        BE-->>FE: Return Generated Content
        FE->>User: Display Preview & Tabs
    end





