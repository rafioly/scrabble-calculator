import { Paper, Typography, Box } from '@mui/material';

type ScoringRulesProps = {
    scoringRules: { [key: string]: number };
};

const ScoringRules = ({ scoringRules }: ScoringRulesProps) => (
    <Paper elevation={3} sx={{ p: 4, borderRadius: 2, textAlign: 'center', width: 200 }}>
        <Typography variant="h5" gutterBottom>
            Scoring Rules
        </Typography>
        <Box
            display="grid"
            gridTemplateColumns="repeat(3, 1fr)"
            gap={2}
            sx={{ mt: 1, textAlign: 'left' }}
        >
            {Object.keys(scoringRules).sort().map((letter) => (
                <Typography key={letter}><strong>{letter}</strong>: {scoringRules[letter]}</Typography>
            ))}
        </Box>
    </Paper>
);

export default ScoringRules;
