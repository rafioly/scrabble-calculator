import { useState, useEffect, useRef } from 'react';
import {
    Typography, Box, TextField, Button, Stack, Dialog, DialogTitle,
    DialogContent, List, ListItem, ListItemText, DialogActions, Alert, Paper
} from '@mui/material';

// --- Type Definitions ---
type ScoringRules = { [key: string]: number };
type LeaderboardEntry = { word: string; score: number };

// --- Constants ---
const TILE_COUNT = 10;

// --- Main Application Component ---
function App() {
    // --- State Management ---
    const [tiles, setTiles] = useState<string[]>(Array(TILE_COUNT).fill(''));
    const [score, setScore] = useState<number>(0);
    const [scoringRules, setScoringRules] = useState<ScoringRules>({});
    const [leaderboard, setLeaderboard] = useState<LeaderboardEntry[]>([]);
    const [isLeaderboardOpen, setLeaderboardOpen] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

    // --- Side Effects ---

    // Fetch scoring rules on initial load
    useEffect(() => {
        const fetchScoringRules = async () => {
            try {
                const response = await fetch('/api/scoring-rules');
                const data = await response.json();
                if (data.success) {
                    setScoringRules(data.data);
                } else {
                    throw new Error(data.message || 'Failed to fetch scoring rules.');
                }
            } catch (e) {
                setError(e instanceof Error ? e.message : 'An unknown error occurred.');
            }
        };
        fetchScoringRules();
    }, []);

    // Recalculate score whenever tiles change
    useEffect(() => {
        const currentWord = tiles.join('').toUpperCase();
        let currentScore = 0;
        for (const char of currentWord) {
            currentScore += scoringRules[char] || 0;
        }
        setScore(currentScore);
    }, [tiles, scoringRules]);

    // --- Event Handlers ---

    const handleTileChange = (e: any, index: number) => {
        const value = e.target.value.toUpperCase().slice(0, 1);
        const newTiles = [...tiles];
        newTiles[index] = value;
        setTiles(newTiles);

        // Move to next input
        if (value && index < TILE_COUNT - 1) {
            inputRefs.current[index + 1]?.focus();
        }
    };

    const handleKeyDown = (e: any, index: number) => {
        if (e.key === 'Backspace' && !tiles[index] && index > 0) {
            inputRefs.current[index - 1]?.focus();
        }
    };

    const handleReset = () => {
        setTiles(Array(TILE_COUNT).fill(''));
        inputRefs.current[0]?.focus();
        setError(null);
        setSuccess(null);
    };

    const handleSaveScore = async () => {
        const word = tiles.join('').trim();
        if (!word) {
            setError('Please enter a word before saving.');
            return;
        }

        try {
            const response = await fetch('/api/leaderboards', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ word }),
            });
            const data = await response.json();

            if (response.ok && data.success) {
                setSuccess(`Successfully saved word: "${word}" with score: ${data.data.score}`);
                setError(null);
            } else {
                throw new Error(data.message || 'Failed to save score.');
            }
        } catch (e) {
            setSuccess(null);
            setError(e instanceof Error ? e.message : 'An unknown error occurred.');
        }
    };

    const handleViewScores = async () => {
        try {
            const response = await fetch('/api/leaderboards?top=10');
            const data = await response.json();
            if (data.success) {
                setLeaderboard(data.data);
                setLeaderboardOpen(true);
                setError(null);
            } else {
                throw new Error(data.message || 'Failed to fetch leaderboard.');
            }
        } catch (e) {
            setError(e instanceof Error ? e.message : 'An unknown error occurred.');
        }
    };

    // --- Render Method ---
    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            minHeight="100vh"
        >
            <Paper elevation={3} sx={{ p: 4, borderRadius: 2, textAlign: 'center', maxWidth: 600 }}>
                <Typography variant="h3" gutterBottom>
                    Scrabble Points Calculator
                </Typography>

                {/* --- Input Tiles --- */}
                <Typography variant="h6" gutterBottom>Enter your word:</Typography>
                <Stack direction="row" spacing={1} justifyContent="center" sx={{ mb: 2 }}>
                    {tiles.map((tile, index) => (
                        <TextField
                            key={index}
                            value={tile}
                            onChange={(e) => handleTileChange(e, index)}
                            onKeyDown={(e) => handleKeyDown(e, index)}
                            inputRef={(el) => (inputRefs.current[index] = el)}
                            inputProps={{ maxLength: 1, style: { textAlign: 'center', fontSize: '1.5rem' } }}
                            sx={{ width: 60 }}
                        />
                    ))}
                </Stack>

                {/* --- Score Display --- */}
                <Typography variant="h4" sx={{ my: 3 }}>
                    Current Score: {score}
                </Typography>

                {/* --- Action Buttons --- */}
                <Stack direction="row" spacing={2} justifyContent="center">
                    <Button variant="outlined" color="secondary" onClick={handleReset}>Reset Tiles</Button>
                    <Button variant="contained" color="primary" onClick={handleSaveScore}>Save Score</Button>
                    <Button variant="contained" color="info" onClick={handleViewScores}>View Top Scores</Button>
                </Stack>

                {/* --- Alerts --- */}
                <Box sx={{ mt: 2, height: 60 }}>
                    {error && <Alert severity="error">{error}</Alert>}
                    {success && <Alert severity="success">{success}</Alert>}
                </Box>

                {/* --- Leaderboard Dialog --- */}
                <Dialog open={isLeaderboardOpen} onClose={() => setLeaderboardOpen(false)} fullWidth>
                    <DialogTitle>Top 10 Scores</DialogTitle>
                    <DialogContent>
                        <List>
                            {leaderboard.map((entry, index) => (
                                <ListItem key={index}>
                                    <ListItemText primary={`${index + 1}. ${entry.word}` } secondary={`Score: ${entry.score}`} />
                                </ListItem>
                            ))}
                        </List>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setLeaderboardOpen(false)}>Close</Button>
                    </DialogActions>
                </Dialog>
            </Paper>
        </Box>
    );
}

export default App;
