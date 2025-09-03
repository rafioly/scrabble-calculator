import { Box, Stack } from '@mui/material';
import ScoreForm from './components/ScoreForm';
import ScoringRules from './components/ScoringRules';
import Leaderboard from './components/Leaderboard';
import { useWordInput } from './hooks/useWordInput';
import { useScoring } from './hooks/useScoring';
import { useLeaderboard } from './hooks/useLeaderboard';

function App() {
    // --- Custom Hooks ---
    const { tiles, inputRefs, handleTileChange, handleKeyDown, handleReset: resetTiles } = useWordInput();
    const { score, scoringRules, error: scoringError } = useScoring(tiles);
    const {
        leaderboard,
        isLeaderboardOpen,
        error: leaderboardError,
        success,
        handleViewScores,
        handleSaveScore,
        closeLeaderboard,
        resetMessages
    } = useLeaderboard();

    // --- Combined Logic ---
    const handleReset = () => {
        resetTiles();
        resetMessages();
    };

    const onSave = () => {
        const word = tiles.join('').trim();
        handleSaveScore(word);
    };

    // Combine errors from different hooks
    const error = scoringError || leaderboardError;

    // --- Render Method ---
    return (
        <Box
            display="flex"
            flexDirection="column"
            justifyContent="center"
            alignItems="center"
            minHeight="100vh"
        >
            <Stack direction="row" spacing={4} alignItems="flex-start">
                <ScoreForm
                    tiles={tiles}
                    score={score}
                    error={error}
                    success={success}
                    inputRefs={inputRefs}
                    handleTileChange={handleTileChange}
                    handleKeyDown={handleKeyDown}
                    handleReset={handleReset}
                    handleSaveScore={onSave}
                    handleViewScores={handleViewScores}
                />
                <ScoringRules scoringRules={scoringRules} />
            </Stack>
            <Leaderboard
                isOpen={isLeaderboardOpen}
                onClose={closeLeaderboard}
                leaderboard={leaderboard}
            />
        </Box>
    );
}

export default App;
