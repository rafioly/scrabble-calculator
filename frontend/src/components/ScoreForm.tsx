import { TextField, Typography, Button, Stack, Box, Alert, Paper } from '@mui/material';
import React from 'react';
import Header from "./Header.tsx";

type ScoreFormProps = {
    tiles: string[];
    score: number;
    error: string | null;
    success: string | null;
    inputRefs: { current: (HTMLInputElement | null)[] };
    handleTileChange: (e: React.ChangeEvent<HTMLInputElement>, index: number) => void;
    handleKeyDown: (e: React.KeyboardEvent<HTMLInputElement>, index: number) => void;
    handleReset: () => void;
    handleSaveScore: () => void;
    handleViewScores: () => void;
};

const ScoreForm = ({ tiles, score, error, success, inputRefs, handleTileChange, handleKeyDown, handleReset, handleSaveScore, handleViewScores }: ScoreFormProps) => (
    <Paper elevation={3} sx={{ p: 4, borderRadius: 2, textAlign: 'center', maxWidth: 600 }}>
        <Header />
        <Typography variant="h6" gutterBottom>Enter your word:</Typography>
        <Stack direction="row" spacing={1} justifyContent="center" sx={{ mb: 2 }}>
            {tiles.map((tile, index) => (
                <TextField
                    key={index}
                    value={tile}
                    onChange={(e: any) => handleTileChange(e, index)}
                    onKeyDown={(e: any) => handleKeyDown(e, index)}
                    onClick={(e: any) => e.target.select()} // Select text on click for easy replacement
                    inputRef={(el) => (inputRefs.current[index] = el)}
                    inputProps={{ maxLength: 1 }}
                    sx={{
                        width: 60,
                        '& .MuiInputBase-input': {
                            textAlign: 'center',
                            fontSize: '1.5rem',
                        },
                    }}
                />
            ))}
        </Stack>

        <Typography variant="h4" sx={{ my: 3 }}>
            Current Score: {score}
        </Typography>

        <Stack direction="row" spacing={2} justifyContent="center">
            <Button variant="outlined" color="secondary" onClick={handleReset}>Reset Tiles</Button>
            <Button variant="contained" color="primary" onClick={handleSaveScore}>Save Score</Button>
            <Button variant="contained" color="info" onClick={handleViewScores}>View Top Scores</Button>
        </Stack>

        <Box sx={{ mt: 2, height: 60 }}>
            {error && <Alert severity="error">{error}</Alert>}
            {success && <Alert severity="success">{success}</Alert>}
        </Box>
    </Paper>
);

export default ScoreForm;
