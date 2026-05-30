# Spring AI in Action -- SKG Java Meetup Demo

A progressive, hands-on demo built for the [SKG Java Meetup](https://www.meetup.com/thessaloniki-java-meetup-group/) (Thessaloniki) showing how to integrate AI capabilities into Spring Boot applications using [Spring AI](https://spring.io/projects/spring-ai).

The project builds an intelligent **Meetup Community Assistant** that can query members, search by name, RSVP to events, and more -- all through natural language.

## Tech Stack

| Component    | Version       |
|-------------|---------------|
| Java         | 25            |
| Spring Boot  | 3.5.14        |
| Spring AI    | 1.0.8         |
| H2 Database  | embedded      |
| Maven        | 3.9+ (wrapper included) |

## Prerequisites

- **Java 25** (or 21+)
- **OpenAI API key** -- get one at [platform.openai.com](https://platform.openai.com/)

That's it. Everything else (Maven, H2, dependencies) is included.

## Setup & Run

1. **Clone the repository**

   ```bash
   git clone <repo-url>
   cd spring-ai
   ```

2. **Set your OpenAI API key** as an environment variable

   ```bash
   # Linux / macOS
   export OPENAI_API_KEY=sk-your-key-here

   # Windows PowerShell
   $env:OPENAI_API_KEY = "sk-your-key-here"
   ```

   Or set it in your IDE's Run Configuration (IntelliJ: Run > Edit Configurations > Environment Variables).

3. **Run the application**

   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:

   ```cmd
   mvnw.cmd spring-boot:run
   ```

4. **Verify** -- open [http://localhost:8080/hi](http://localhost:8080/hi) and you should see `Hello SKG Java Meetup!`

## Features Covered

### Simple Chat (`/chat`)
Basic LLM integration with `ChatClient` -- send a message, get a response.

```
GET /chat?message=Hi from SKG Java Meetup!

--> "Hello! It's great to hear from the SKG Java Meetup! How can I assist you today?"
```

### System Prompt
Configure the AI's personality and behavior globally via `.defaultSystem()`. In this demo we use a fun sarcastic QA engineer persona:

```
GET /chat?message=Hi from SKG Java Meetup!

--> "What's more pointless than a meeting of minds that can't even meet a deadline?"
```

### Prompt Decoration (`/chat/suggest`)
Enhance user input with additional context, instructions, and style hints using parameterized prompt templates.

```
GET /chat/suggest?message=Can you suggest a programming language for a frontend app?

--> "What's a bird without wings, and a dev without skills? Pick a language that doesn't
     require a manual for dummies. Try JavaScript; it's the one that doesn't bite back
     — unlike your code."
```

### Structured Output (`/chat/suggest/dto`)
Get typed Java objects (records) from the AI instead of raw text -- using `.entity()`. No parsing, no regex.

```
GET /chat/suggest/dto?message=What's the worst frontend language?

--> {
      "reason": "If you need to ask, maybe try knitting instead of coding.",
      "language": {
        "name": "HTML",
        "creator": "Some questionable genius",
        "createdAt": "1991-01-01",
        "popularity": 100.0
      }
    }
```

### Chat Memory (`/chat/mem`)
Multi-turn conversations with `MessageChatMemoryAdvisor` and `conversationId` for session isolation.

```
GET /chat/mem?message=My name is Maria and I have a cat named Souvlaki&conversationId=001
--> "Nice to meet you, Maria! ..."

GET /chat/mem?message=What's my cat's name?&conversationId=001
--> "Your cat's name is Souvlaki!"

GET /chat/mem?message=What's my cat's name?&conversationId=002
--> "I don't have that information."   (different session -- no memory)
```

### Tool Calling (`/meetup/ai/chat`)
The AI autonomously calls your business logic -- querying the database, searching members, RSVPing to events -- based on natural language input. Uses `@Tool` and `@ToolParam` annotations.

```
GET /meetup/ai/chat?message=How many members has this Community?&conversationId=001
--> "The SKG Java Meetup community currently has **20 members**."
    (AI called: getAllMembers)

GET /meetup/ai/chat?message=Who is the organizer? How can I contact him?&conversationId=001
--> "The organizer is **Eleftherios Chrysochoidis**. He has attended **58 events**.
     Contact him at eleftherios.chrysochoidis@example.com."
    (AI called: searchMembersByName → findMemberByEmail)

GET /meetup/ai/chat?message=Mr Manolis has registered to attend an event. Let's update our records.&conversationId=001
--> "Mr. Theodoros Manolis has been successfully RSVPed! His total RSVPs now stand at 61."
    (AI called: searchMembersByName → rsvpToEvent)
```

### MCP Server (`/sse`)
Exposes the same tools over the Model Context Protocol, allowing external AI clients (Claude Desktop, Claude Code, Cursor, etc.) to discover and use your app's capabilities.

```
# Connect from Claude Code:
> setup the skg-java-meetup-mcp from http://localhost:8080/sse

# Then ask naturally:
> who has attended the most SKG Java meetup events?
--> The top 5 most frequent attendees are: 1. Dimitrios Vlahos (61 RSVPs), ...
    (Claude called: getTopAttendees via MCP)
```

## API Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /hi` | Health check |
| `GET /chat?message=` | Simple chat |
| `GET /chat/suggest?message=` | Chat with prompt decoration |
| `GET /chat/suggest/dto?message=` | Structured output (JSON) |
| `GET /chat/mem?message=&conversationId=` | Chat with memory |
| `GET /meetup/ai/chat?message=&conversationId=` | AI assistant with tool calling |
| `GET /meetup/members` | List all members (REST) |
| `GET /meetup/members/top?limit=` | Top attendees (REST) |
| `GET /sse` | MCP server endpoint |
| `/h2-console` | H2 database console |

## Workshop Guide

A comprehensive step-by-step guide is available for reproducing this workshop. See the `workshop_guide/` directory or ask the presenters for a copy.

## Resources

- [Spring AI Documentation](https://spring.io/projects/spring-ai)
- [Spring AI OpenAI Chat](https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html)
- [Spring AI Tool Calling](https://docs.spring.io/spring-ai/reference/api/tools.html)
- [Spring AI Chat Memory](https://docs.spring.io/spring-ai/reference/api/chat-memory.html)
- [Spring AI MCP Overview](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)
- [Baeldung Spring AI](https://www.baeldung.com/spring-ai)
