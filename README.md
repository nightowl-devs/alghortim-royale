# alghortim-royale
🔌 Computer plays Clash Royale, very cool.

## Project Overview

This project is an automated Clash Royale bot designed to play using the **X-Bow deck** strategy. The primary objective is to create an intelligent system that can effectively compete in Clash Royale matches by leveraging advanced game state analysis.

## Current Goals

### X-Bow Deck Strategy
The bot is specifically designed to play X-Bow decks, which are defensive siege decks that rely on:
- Placing X-Bow at the bridge to target the opponent's tower
- Defending the X-Bow with defensive buildings and troops
- Managing elixir efficiently to maintain defensive superiority
- Cycling cards quickly to get back to key defensive pieces

### Opponent Analysis System
The core innovation of this project is real-time opponent tracking through network packet analysis:

#### Cycle Tracking
- **Card Rotation Monitoring**: Track which cards the opponent has played and predict their current hand
- **Card Count Analysis**: Monitor how many cards the opponent has cycled through
- **Next Card Prediction**: Anticipate what cards the opponent is likely to have available

#### Elixir Management
- **Current Elixir Tracking**: Monitor the opponent's elixir count in real-time
- **Elixir Advantage Calculation**: Determine when we have an elixir advantage to make aggressive plays
- **Spend Pattern Analysis**: Learn the opponent's elixir spending patterns

### Technical Approach

#### Network Packet Monitoring
The system works by intercepting and analyzing network packets between the Clash Royale app and Supercell's servers:
- **Packet Interception**: Capture network traffic to/from the game client
- **Protocol Analysis**: Decode Supercell's game protocol to extract game state information
- **Real-time Processing**: Process packet data in real-time to maintain current game state
- **State Reconstruction**: Build a complete picture of both player's game states

This approach provides unprecedented insight into the opponent's current situation, allowing for optimal decision-making that would be impossible with visual analysis alone.

## Development Status

🚧 **Early Development Phase** - Currently focused on establishing the network monitoring foundation and game state tracking systems.
